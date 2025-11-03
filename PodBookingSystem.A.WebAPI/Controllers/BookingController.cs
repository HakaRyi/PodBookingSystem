using Microsoft.AspNetCore.Mvc;
using PodBookingSystem.B.ServiceLayer;
using PodBookingSystem.C.RepositoryLayer.Models;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace PodBookingSystem.A.WebAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class BookingController : ControllerBase
    {
        private readonly BookingService _bookingService;
        private readonly IEmailService _emailService;
        public BookingController(BookingService bookingService, IEmailService emailService)
        {
            _bookingService = bookingService;
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

        // POST api/<BookingController>
        [HttpPost("{userId}")]
        public async Task<int> Post([FromBody] Booking booking, [FromRoute] int userId)
        {
            return await _bookingService.CreateAsync(booking, userId);
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
    }
}
