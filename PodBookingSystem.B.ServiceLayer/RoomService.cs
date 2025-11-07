using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;
using PodBookingSystem.C.RepositoryLayer.DBContext;
using PodBookingSystem.C.RepositoryLayer.Models;
using PodBookingSystem.C.RepositoryLayer.UnitOfWorks;

namespace PodBookingSystem.B.ServiceLayer
{
    public class RoomService
    {
        private readonly IUnitOfWork _unitOfWork;
        private readonly ILogger<AccountService> _logger;
        private readonly IConfiguration _configuration;
        private readonly PodBookingSystemContext _context;
        public RoomService(IUnitOfWork unitOfWork, ILogger<AccountService> logger, IConfiguration configuration, PodBookingSystemContext context)
        {
            _unitOfWork = unitOfWork;
            _logger = logger;
            _configuration = configuration;
            _context = context;
        }
        public async Task<List<Room>> GetRooms()
        {
            try
            {
                return await _unitOfWork.RoomRepository.GetRoomsAsync();
            }
            catch (Exception ex)
            {
            }
            return new List<Room>();
        }
        public async Task<List<Room>> Get3Rooms()
        {
            try
            {
                return await _unitOfWork.RoomRepository.Get3RoomsAsync();
            }
            catch (Exception ex)
            {
            }
            return new List<Room>();
        }
        public async Task<List<Room>> GetNewestRoom()
        {
            try
            {
                return await _unitOfWork.RoomRepository.GetNewestRoomAsync();
            }
            catch (Exception ex)
            {
            }
            return new List<Room>();
        }
        public async Task<Room> GetRoomById(int id)
        {
            try
            {
                return await _unitOfWork.RoomRepository.GetRoomAsync(id);
            }
            catch (Exception e)
            {
                _logger.LogError(e, "Lỗi khi lấy phòng theo Id: {RoomId}", id);
                return null; 
            }
            
        }
        public async Task<int> Create(Room room) 
        {
            try
            {
                
                room.Status = "AVAILABLE";

                
                await _unitOfWork.RoomRepository.CretaeAsync(room);
                return 1; 
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Lỗi khi tạo phòng mới"); 
                return 0;
            }
        }
        public async Task<int> Update(int id, Room room)
        {
            try
            {
                
                room.RoomId = id;

                
                var existingRoom = await GetRoomById(id);
                if (existingRoom == null)
                {
                    return 0; 
                }

                return await _unitOfWork.RoomRepository.UpdateAsync(room);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Lỗi khi cập nhật phòng: {RoomId}", id);
                return 0;
            }
        }
        public async Task<bool> DeleteAsync(int id)
        {
            // Dùng FindAsync để lấy đối tượng được theo dõi
            var room = await _context.Rooms.FindAsync(id);

            if (room != null)
            {
                _context.Rooms.Remove(room);
                await _context.SaveChangesAsync();
                return true;
            }
            return false;
        }

    }
}
