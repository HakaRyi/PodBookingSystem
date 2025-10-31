//using System;
//using System.Collections.Generic;
//using System.Linq;
//using System.Text;
//using System.Threading.Tasks;
//using Microsoft.Extensions.Configuration;
//using Microsoft.Extensions.Logging;
//using Microsoft.Extensions.Options;
//using Net.payOS;
//using Net.payOS.Errors;
//using Net.payOS.Types;
//using PodBookingSystem.C.RepositoryLayer.UnitOfWorks;
//using Service.Config;

//namespace PodBookingSystem.B.ServiceLayer
//{
//    public class PayOsPaymentService
//    {
//        private readonly PayOS _payOs;
//        private readonly IUnitOfWork _unitOfWork;
//        private readonly ILogger<AccountService> _logger;
//        private readonly IConfiguration _configuration;
//        public PayOsPaymentService(IOptions<PayOsSettings> settings, IUnitOfWork unitOfWork, ILogger<AccountService> logger, IConfiguration configuration)
//        {
//            var config = settings.Value;
//            _payOs = new PayOS(config.ClientId, config.ApiKey, config.ChecksumKey);
//            _unitOfWork = unitOfWork;
//            _logger = logger;
//            _configuration = configuration;
//        }
//        public async Task<string> CreatePaymentUrlAsync(int bookingId, decimal amount, string returnUrl)
//        {
//            var booking = await _unitOfWork.BookingRepository.GetByIdAsync(bookingId);
//            if (booking == null)
//                throw new Exception($"booking not found with ID = {booking}");

//            long orderCode = long.Parse($"{booking}{DateTime.UtcNow:MMddHHmmss}");

//            //Danh sách sản phẩm
//            var itemList = booking.BookingDetails.Select(od => new ItemData(
//                od.Room?.Name ?? "Room",
//                od.NumberOfFood,
//                (int)(od.FoodPrice ?? 0)
//            )).ToList();

//            var paymentData = new PaymentData(
//                orderCode: orderCode,
//                amount: (int)amount,
//                description: $"don hang #{orderId}-Gusto",
//                items: itemList,
//                cancelUrl: "http://localhost:3000/restaurants/",
//                returnUrl: $"http://localhost:3000/profile/bkh"
//            );

//            try
//            {
//                var result = await _payOs.createPaymentLink(paymentData);
//                Console.WriteLine($"Created PayOS payment link for order {orderId} with orderCode={orderCode}");
//                return result.checkoutUrl;
//            }
//            catch (PayOSError ex)
//            {
//                Console.WriteLine("Lỗi PayOS: " + ex.ToString());
//                throw new ApplicationException("Lỗi khi tạo link thanh toán PayOS: " + ex.Message);
//            }
//        }
//    }
//}
