using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using Net.payOS;
using Net.payOS.Errors;
using Net.payOS.Types;
using PodBookingSystem.B.ServiceLayer.DTO.Request;
using PodBookingSystem.C.RepositoryLayer.Models;
using PodBookingSystem.C.RepositoryLayer.UnitOfWorks;


namespace PodBookingSystem.B.ServiceLayer
{
    public class PayOsPaymentService
    {
        private readonly PayOS _payOs;
        private readonly IUnitOfWork _unitOfWork;
        private readonly ILogger<AccountService> _logger;
        private readonly IConfiguration _configuration;
        public PayOsPaymentService(IOptions<PayOsSettings> settings
            , IUnitOfWork unitOfWork, ILogger<AccountService> logger, IConfiguration configuration)
        {
            var config = settings.Value;
            _payOs = new PayOS(config.ClientId, config.ApiKey, config.ChecksumKey);
            _unitOfWork = unitOfWork;
            _logger = logger;
            _configuration = configuration;
        }
        public async Task<string> CreatePaymentUrlAsync(int bookingId, decimal amount, string returnUrl, string cancelUrl)
        {
            var booking = await _unitOfWork.BookingRepository.GetByIdAsync(bookingId);
            if (booking == null)
                throw new Exception($"booking not found with ID = {booking}");

            long orderCode = long.Parse($"{bookingId}{DateTime.UtcNow:MMddHHmmss}");

            //Danh sách sản phẩm
            var itemList = booking.BookingDetails.Select(od => new ItemData(
                od.Room?.Name ?? "Room",
                1,
                (int)(od.TotalPrice)
            )).ToList();

            var paymentData = new PaymentData(
                orderCode: orderCode,
                amount: (int)amount,
                description: $"POD #{bookingId}:paid",
                items: itemList,
                cancelUrl: cancelUrl,
                returnUrl: returnUrl
            );

            try
            {
                var result = await _payOs.createPaymentLink(paymentData);
                Console.WriteLine($"Created PayOS payment link for order {bookingId} with orderCode={orderCode}");
                return result.checkoutUrl;
            }
            catch (PayOSError ex)
            {
                Console.WriteLine("Lỗi PayOS: " + ex.ToString());
                throw new ApplicationException("Lỗi khi tạo link thanh toán PayOS: " + ex.Message);
            }
        }
        public async Task<WebhookData> VerifyWebhook(WebhookType webhookBody)
        {
            Console.WriteLine("Webhook received, verifying...");

            var data = _payOs.verifyPaymentWebhookData(webhookBody);
            if (data == null)
            {   
                Console.WriteLine("verifyPaymentWebhookData returned null");
                return null;
            }

            Console.WriteLine($"Webhook verified: {System.Text.Json.JsonSerializer.Serialize(data)}");

            if (data.code != "00")
            {
                Console.WriteLine($"Payment not successful (code={data.code})");
                return null;
            }
            string orderCodeStr = data.orderCode.ToString();


            string orderIdStr = orderCodeStr[..^10]; //bo 10 ky tu cuoi
            short bookingId = short.Parse(orderIdStr);

            Console.WriteLine($"Extracted orderId = {bookingId} from orderCode = {data.orderCode}");

            var booking = await _unitOfWork.BookingRepository.GetByIdForUpdateAsync(bookingId);
            if (booking == null || booking.Status != "PENDING")
            {
                Console.WriteLine($"booking not found: {bookingId}");
                return null;
            }
            booking.Status = "BOOKED";
            await _unitOfWork.BookingRepository.UpdateAsync(booking);
            var payment = new Payment
            {
                BookingId = booking.BookingId,
                TotalAmount = booking.Total,
 
            };
            await _unitOfWork.PaymentRepository.CreateAsync(payment);
           

            Console.WriteLine($"booking {bookingId} marked as Paid");
            Console.WriteLine($"Webhook xử lý thành công cho bookingId {bookingId}");

            return data;
        }
    }
}
