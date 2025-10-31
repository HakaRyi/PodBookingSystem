using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;
using PodBookingSystem.C.RepositoryLayer.Models;
using PodBookingSystem.C.RepositoryLayer.UnitOfWorks;

namespace PodBookingSystem.B.ServiceLayer
{
    public class RoomService
    {
        private readonly IUnitOfWork _unitOfWork;
        private readonly ILogger<AccountService> _logger;
        private readonly IConfiguration _configuration;
        public RoomService(IUnitOfWork unitOfWork, ILogger<AccountService> logger, IConfiguration configuration)
        {
            _unitOfWork = unitOfWork;
            _logger = logger;
            _configuration = configuration;
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
        public async Task<Room> GetRoomById(int id)
        {
            try
            {
                return await _unitOfWork.RoomRepository.GetRoomAsync(id);
            }
            catch(Exception e)
            {

            }
            return new Room();
        }
        public async Task<int> Create(Room room)
        {
            try
            {
                room = new Room()
                {
                    Status = "AVAILABLE"
                };
                await _unitOfWork.RoomRepository.CretaeAsync(room);
                return 1;


            }
            catch (Exception ex) { }
            return 0;
        }
        public async Task<int> Update(int id,Room room)
        {
            try
            {
                var exisitingRoom = await GetRoomById(id);
                if (exisitingRoom != null)
                {
                    return await _unitOfWork.RoomRepository.UpdateAsync(room);
                }
                return 0;
                
            }
            catch (Exception ex) { }
            return 0;
        }
        public async Task<bool> Delete(int id)
        {
            try
            {
                var exisitingRoom = await GetRoomById(id);
                if (exisitingRoom != null)
                {
                    return await _unitOfWork.RoomRepository.DeleteAsync(id);
                }
                return false;

            }
            catch (Exception ex) { }
            return false;
        }

    }
}
