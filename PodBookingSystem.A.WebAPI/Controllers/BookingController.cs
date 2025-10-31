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
        public BookingController(BookingService bookingService)
        {
            _bookingService = bookingService;
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
        // DELETE api/<BookingController>/5
        [HttpDelete("{id}")]
        public async Task<bool> Delete(int id)
        {
            return await _bookingService.DeleteAsync(id);
        }
    }
}
