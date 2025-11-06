using Microsoft.AspNetCore.Mvc;
using PodBookingSystem.B.ServiceLayer;
using PodBookingSystem.C.RepositoryLayer.DBContext;
using PodBookingSystem.C.RepositoryLayer.Models;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace PodBookingSystem.A.WebAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class RoomController : ControllerBase
    {
        private readonly RoomService _service;

        public RoomController(RoomService service)
        {
            _service = service;
        }
        // GET: api/<RoomController>
        [HttpGet]
        public async Task<List<Room>> Get()
        {
            return await _service.GetRooms();
        }
        [HttpGet("3latest")]
        public async Task<List<Room>> Get3()
        {
            return await _service.Get3Rooms();
        }
        [HttpGet("newest")]
        public async Task<List<Room>> GetNewest()
        {
            return await _service.GetNewestRoom();
        }

        // GET api/<RoomController>/5
        [HttpGet("{id}")]
        public async Task<Room> Get(int id)
        {
            return await _service.GetRoomById(id);
        }

        // POST api/<RoomController>
        [HttpPost]
        public async Task<int> Post([FromBody]Room room)
        {
            return await _service.Create(room);
        }

        // PUT api/<RoomController>/5
        [HttpPut("{id}")]
        public async Task<int> Put([FromRoute]int id, [FromBody] Room room)
        {
            return await _service.Update(id,room);
        }

        // DELETE api/<RoomController>/5
        [HttpDelete("{id}")]
        public async Task<bool> Delete([FromRoute] int id)
        {
            return await _service.Delete(id);
        }
    }
}
