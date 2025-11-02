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
        public BookingController(BookingService bookingService, PayOsPaymentService payOsPaymentService, AccountService accountService )
        {
            _bookingService = bookingService;
            _payOsPaymentService = payOsPaymentService;
            _accountService = accountService;
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
        public async Task<Booking> GetPendingByUserId()
        {
            var decodedToken = await FirebaseTokenHelper.VerifyFirebaseTokenAsync(Request);
            var email = decodedToken.Claims.ContainsKey("email")
            ? decodedToken.Claims["email"].ToString()
            : null;
            var user = await _accountService.GetAccountByEmailAsync(email);
            return await _bookingService.GetBookingAsync(user.AccId);
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
        public async Task<int> Post2()
        {
            var decodedToken = await FirebaseTokenHelper.VerifyFirebaseTokenAsync(Request);
            var email = decodedToken.Claims.ContainsKey("email")
            ? decodedToken.Claims["email"].ToString()
            : null;
            var user = await _accountService.GetAccountByEmailAsync(email);
            return await _bookingService.CreateAsync2(1);
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
                var returnUrl = "http://localhost:3000/profile/bkh";
                booking.Total = booking.Total;

                var amount = booking.Total;
                if (amount < 1000) amount = 3000;


                var link = await _payOsPaymentService.CreatePaymentUrlAsync(bookingId, amount, returnUrl);
                return Ok(new { checkoutUrl = link });
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }
    }
}
