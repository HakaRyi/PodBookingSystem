    using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using PodBookingSystem.B.ServiceLayer;
using PodBookingSystem.B.ServiceLayer.DTO.Request;

namespace PodBookingSystem.A.WebAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class RoomSlotController : ControllerBase
    {
        private readonly RoomSlotService _roomSlotService;

        public RoomSlotController(RoomSlotService roomSlotService)
        {
            _roomSlotService = roomSlotService;
        }

        [HttpGet]
        public async Task<IActionResult> GetAll() => Ok(await _roomSlotService.GetAllAsync());

        [HttpGet("{roomId}/{slotId}")]
        public async Task<IActionResult> GetById(int roomId, int slotId)
        {
            var result = await _roomSlotService.GetByIdAsync(roomId, slotId);
            return result == null ? NotFound() : Ok(result);
        }

        [HttpPost]
        public async Task<IActionResult> Create(RoomSlotRequest dto)
        {
            var result = await _roomSlotService.CreateAsync(dto);
            return result > 0 ? Ok("Created successfully") : BadRequest("Failed to create");
        }

        [HttpPut]
        public async Task<IActionResult> Update(RoomSlotRequest dto)
        {
            var result = await _roomSlotService.UpdateAsync(dto);
            return result > 0 ? Ok("Updated successfully") : NotFound();
        }

        [HttpDelete("{roomId}/{slotId}")]
        public async Task<IActionResult> Delete(int roomId, int slotId)
        {
            var result = await _roomSlotService.DeleteAsync(roomId, slotId);
            return result > 0 ? Ok("Deleted successfully") : NotFound();
        }
    }
}
