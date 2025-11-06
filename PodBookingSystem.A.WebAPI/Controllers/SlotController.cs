using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using PodBookingSystem.B.ServiceLayer;
using PodBookingSystem.B.ServiceLayer.DTO.Request;

namespace PodBookingSystem.A.WebAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class SlotController : ControllerBase
    {
        private readonly SlotService _slotService;

        public SlotController(SlotService slotService)
        {
            _slotService = slotService;
        }
        [HttpGet("available")]
        public async Task<IActionResult> GetAvailableSlots([FromQuery] int roomId, [FromQuery] DateOnly bookingDate)
        {
            if (roomId <= 0 || bookingDate == default)
                return BadRequest("thieu roomId or bookingDate");

            var availableSlots = await _slotService.GetAvailableSlotsAsync(roomId, bookingDate);
            return Ok(availableSlots);
        }

        [HttpGet]
        public async Task<IActionResult> GetAll() => Ok(await _slotService.GetAllAsync());

        [HttpGet("{id}")]
        public async Task<IActionResult> GetById(int id)
        {
            var result = await _slotService.GetByIdAsync(id);
            return result == null ? NotFound() : Ok(result);
        }

        [HttpPost]
        public async Task<IActionResult> Create(SlotRequest dto)
        {
            var result = await _slotService.CreateAsync(dto);
            return result > 0 ? Ok("Created successfully") : BadRequest("Failed to create");
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> Update(int id, SlotRequest dto)
        {
            var result = await _slotService.UpdateAsync(id, dto);
            return result > 0 ? Ok("Updated successfully") : NotFound();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(int id)
        {
            var result = await _slotService.DeleteAsync(id);
            return result > 0 ? Ok("Deleted successfully") : NotFound();
        }
    }
}