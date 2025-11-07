using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using PodBookingSystem.B.ServiceLayer;
using PodBookingSystem.B.ServiceLayer.Helpers;
using PodBookingSystem.C.RepositoryLayer.Models;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace PodBookingSystem.A.WebAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class BookingController : ControllerBase
    {
        private readonly BookingService _bookingService;
        private readonly PayOsPaymentService _payOsPaymentService;
        private readonly AccountService _accountService;
        private readonly IEmailService _emailService;
        public BookingController(BookingService bookingService, PayOsPaymentService payOsPaymentService, AccountService accountService, IEmailService emailService)
        {
            _bookingService = bookingService;
            _payOsPaymentService = payOsPaymentService;
            _accountService = accountService;
            _emailService = emailService;
        }

        // GET: api/<BookingController>
        [HttpGet]
        public async Task<List<Booking>> Get()
        {
            return await _bookingService.GetAllAsync();
        }

        // GET api/<BookingController>/5
        [HttpGet("{id}")]
        public async Task<Booking> GetById(int id)
        {
            return await _bookingService.GetBookingAsync(id);
        }
        [HttpGet("pendingBooking")]
        public async Task<IActionResult> GetPendingByUserId()
        {
            var decodedToken = await FirebaseTokenHelper.VerifyFirebaseTokenAsync(Request);
            var email = decodedToken.Claims.ContainsKey("email")
            ? decodedToken.Claims["email"].ToString()
            : null;
            var user = await _accountService.GetAccountByEmailAsync(email);
            var booking = await _bookingService.GetBookingPendingAsync(user.AccId);
            if (booking == null)
            {
                return Ok(new { message = "Không có booking nào đang chờ" });
            }


            return Ok(new
            {
                message = "Có booking đang chờ xử lý",
                bookingId = booking.BookingId
            });
        }
        [HttpGet("pendingBooking2")]
        public async Task<IActionResult> GetPendingByUserId2()
        {
            var decodedToken = await FirebaseTokenHelper.VerifyFirebaseTokenAsync(Request);
            var email = decodedToken.Claims.ContainsKey("email")
            ? decodedToken.Claims["email"].ToString()
            : null;
            var user = await _accountService.GetAccountByEmailAsync(email);
            var booking = await _bookingService.GetBookingPendingAsync(user.AccId);
            if (booking == null)
            {
                return Ok(new { message = "Không có booking nào đang chờ" });
            }


            return Ok(new
            {
                booking
            });
        }


        // POST api/<BookingController>
        [HttpPost]
        [Authorize]
        public async Task<int> Post([FromBody] Booking booking)
        {
            var decodedToken = await FirebaseTokenHelper.VerifyFirebaseTokenAsync(Request);
            var email = decodedToken.Claims.ContainsKey("email")
            ? decodedToken.Claims["email"].ToString()
            : null;
            var user = await _accountService.GetAccountByEmailAsync(email);
            return await _bookingService.CreateAsync(booking, user.AccId);
        }
        [HttpPost("createBooking")]
        [Authorize]
        public async Task<IActionResult> Post2()
        {
            var decodedToken = await FirebaseTokenHelper.VerifyFirebaseTokenAsync(Request);
            if (decodedToken == null) return Unauthorized("Token invalid");

            var email = decodedToken.Claims["email"]?.ToString();
            var user = await _accountService.GetAccountByEmailAsync(email);
            if (user == null) return NotFound("User not found");

            try
            {
                var bookingId = await _bookingService.CreateAsync2(user.AccId);
                return Ok(new { bookingId });
            }
            catch (InvalidOperationException ex)
            {
                return Conflict(ex.Message);
            }
        }

        // PUT api/<BookingController>/5
        [HttpPut("{id}")]
        public async Task<int> Put(int id, Booking booking)
        {
            return await _bookingService.UpdateAsync(id, booking);
        }
        [HttpPut("Booked/{id}")]
        public async Task<int> UpdateBooked(int id)
        {
            return await _bookingService.UpdateStatusBOOKEDAsync(id);
        }
        [HttpPut("Check-in/{id}")]
        public async Task<int> UpdateCheckin(int id)
        {
            return await _bookingService.UpdateStatusCHECKINAsync(id);
        }
        [HttpPut("Checkout/{id}")]
        public async Task<int> UpdateCheckout(int id)
        {
            return await _bookingService.UpdateStatusCHECKOUTAsync(id);
        }
        [HttpPut("Done/{id}")]
        public async Task<int> UpdateDone(int id)
        {
            return await _bookingService.UpdateStatusDONEAsync(id);
        }
        [HttpPut("Available/{id}")]
        public async Task<int> UpdateAvailable(int id)
        {
            return await _bookingService.UpdateStatusAVAILABLEAsync(id);
        }

        [HttpPut("Cancel/{id}")]
        public async Task<IActionResult> UpdateCancel(int id, [FromQuery] string message)
        {
            var result = await _bookingService.UpdateStatusCANCELEDAsync(id, message);
            if (!result.Success)
                return BadRequest("Hủy đặt phòng thất bại");
            var booking = result.booking;

            if (booking?.User?.Email != null)
            {
                await _emailService.SendEmailAsync(
                    booking.User.Email,
                    "Thông báo hủy đặt phòng",
                    $@"Xin chào {booking.User.Name},
                    Booking #{booking.BookingId} của bạn đã bị hủy.

                    Lý do: {message}
                    
                    Mọi khoảng tiền mà bạn đã thanh toán sẽ được hoàn lại trong vòng 12h sau khi nhận được thông báo này. Vui lòng giữ tin nhắn này để làm bằng chứng trước tòa
                    Nếu có thắc mắc, vui lòng liên hệ đội ngũ hỗ trợ.
                    Trân trọng,
                    G6 Pod Booking System."
                                    );
            }

            return Ok(1);

        }
        // DELETE api/<BookingController>/5
        [HttpDelete("{id}")]
        public async Task<bool> Delete(int id)
        {
            return await _bookingService.DeleteAsync(id);
        }
        [HttpPost("pay/{bookingId}")]
        public async Task<IActionResult> Pay(int bookingId)
        {
            try
            {
                var booking = await _bookingService.GetBookingAsync(bookingId);
                if (booking == null || booking.Status != "PENDING")
                    return BadRequest("Booking không hợp lệ hoặc đã được xử lý.");
                var returnUrl = "g6podbookingsystem://payment-success?status=success";
                var cancelUrl = "g6podbookingsystem://payment-cancel?status=cancel";
                booking.Total = booking.Total;

                var amount = booking.Total;
                if (amount < 1000) amount = 3000;


                var link = await _payOsPaymentService.CreatePaymentUrlAsync(bookingId, amount, returnUrl, cancelUrl);
                return Ok(new { checkoutUrl = link });
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }
        [HttpGet("user/{userId}/history")]
        public async Task<IActionResult> GetUserBookingHistory(int userId)
        {
            var history = await _bookingService.GetUserBookingHistoryAsync(userId);

            if (!history.Any())
                return NotFound(new { message = "Không có lịch sử đặt chỗ nào cho user này." });

            return Ok(history);
        }
    }
}
