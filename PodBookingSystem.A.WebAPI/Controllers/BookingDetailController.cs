using Microsoft.AspNetCore.Mvc;
using PodBookingSystem.B.ServiceLayer;
using PodBookingSystem.B.ServiceLayer.DTO.Request;
using PodBookingSystem.B.ServiceLayer.Helpers;
using PodBookingSystem.C.RepositoryLayer.Models;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace PodBookingSystem.A.WebAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class BookingDetailController : ControllerBase
    {
        private readonly BookingDetailService _detailService;
        private readonly BookingService _bookingService;
        private readonly AccountService _accountService;
        public BookingDetailController(BookingDetailService detailService, BookingService bookingService, AccountService accountService)
        {
            _detailService = detailService;
            _bookingService = bookingService;
            _accountService = accountService;
        }
        // GET: api/<BookingDetailController>
        [HttpGet]
        public async Task<List<BookingDetail>> Get()
        {
            return await _detailService.GetAllAsync();
        }

        // GET api/<BookingDetailController>/5
        [HttpGet("{id}")]
        public async Task<BookingDetail> Get(int id)
        {
            return await _detailService.GetByIdAsync(id);
        }

        // POST api/<BookingDetailController>
        [HttpPost("create/{bookingId}/{roomId}")]
        public async Task<IActionResult> Create([FromBody] CreateDetailDto request, [FromRoute] int bookingId,int roomId)
        {
            try
            {
                var decodedToken = await FirebaseTokenHelper.VerifyFirebaseTokenAsync(Request);
                var email = decodedToken.Claims.ContainsKey("email")
                ? decodedToken.Claims["email"].ToString()
                : null;
                var user = await _accountService.GetAccountByEmailAsync(email);

                var booking = await _bookingService.GetBookingAsync(bookingId);
                if (booking == null || booking.UserId != user.AccId)
                    return Forbid("Booking không thuộc về bạn!");

                var detailId = await _detailService.Create(bookingId, roomId, request);

                return Ok(new
                {
                    message = "Đã thêm phòng thành công!",
                    detailId,
                    //totalBooking = booking.Total,
                    slots = request.Slots.Select(s => new { s.SlotId, s.BookingDate })
                });
            }
            catch (Exception ex)
            {
                return BadRequest(new { error = ex.Message });
            }
        }

        // PUT api/<BookingDetailController>/5
        [HttpPut("{detailId}")]
        public async Task<IActionResult> Update(int detailId, [FromBody] UpdateDetailDto request)
        {
            try
            {
                var decodedToken = await FirebaseTokenHelper.VerifyFirebaseTokenAsync(Request);
                var email = decodedToken.Claims.ContainsKey("email")
                ? decodedToken.Claims["email"].ToString()
                : null;

                var detail = await _detailService.GetByIdAsync(detailId);
                if (detail == null) return NotFound("Không tìm thấy chi tiết đặt phòng!");

                await _detailService.Update(detailId, request);

                return Ok(new
                {
                    message = "Cập nhật thành công!",
                    detailId,
                    newPrice = request.BookingType == "DAY"
                        ? "Toàn ngày"
                        : $"{request.Slots.Count} slot × giờ"
                });
            }
            catch (Exception ex)
            {
                return BadRequest(new { error = ex.Message });
            }
        }

        // DELETE api/<BookingDetailController>/5
        [HttpDelete("{detailId}")]
        public async Task<IActionResult> Delete(int detailId)
        {
            try
            {
    
                var detail = await _detailService.GetByIdAsync(detailId);
                if (detail == null) return NotFound();

                var success = await _detailService.Delete(detailId);

                return success
                    ? Ok(new { message = "Đã xóa phòng!"})
                    : BadRequest("Xóa thất bại!");
            }
            catch (Exception ex)
            {
                return BadRequest(new { error = ex.Message });
            }
        }
    }
}
