using PodBookingSystem.C.RepositoryLayer.Models;
using PodBookingSystem.C.RepositoryLayer;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PodBookingSystem.B.ServiceLayer.DTO.Response;
using PodBookingSystem.B.ServiceLayer.DTO.Request;
using Microsoft.EntityFrameworkCore;
using PodBookingSystem.C.RepositoryLayer.DBContext;

namespace PodBookingSystem.B.ServiceLayer
{
    public class RoomSlotService
    {
        private readonly RoomSlotRepository _roomSlotRepository;
        private readonly PodBookingSystemContext _context; // ✅ thêm dòng này

        public RoomSlotService(RoomSlotRepository roomSlotRepository, PodBookingSystemContext context)
        {
            _roomSlotRepository = roomSlotRepository;
            _context = context; // ✅ gán DbContext vào
        }

        public async Task<List<RoomSlotResponse>> GetAllAsync()
        {
            var data = await _roomSlotRepository.GetAllAsync();
            return data.Select(rs => new RoomSlotResponse
            {
                RoomId = rs.RoomId,
                SlotId = rs.SlotId,
                BookingId = rs.BookingId,
                BookingDate = rs.BookingDate
            }).ToList();
        }

        public async Task<RoomSlotResponse> GetByIdAsync(int roomId, int slotId)
        {
            var rs = await _roomSlotRepository.GetByIdAsync(roomId, slotId);
            if (rs == null) return null;

            return new RoomSlotResponse
            {
                RoomId = rs.RoomId,
                SlotId = rs.SlotId,
                BookingId = rs.BookingId,
                BookingDate = rs.BookingDate
            };
        }

        public async Task<int> CreateAsync(RoomSlotRequest dto)
        {
            var slotExists = await _context.Slots.AnyAsync(s => s.SlotId == dto.SlotId);
            if (!slotExists)
                throw new Exception($"SlotId {dto.SlotId} không tồn tại trong bảng Slot.");

            var roomExists = await _context.Rooms.AnyAsync(r => r.RoomId == dto.RoomId);
            if (!roomExists)
                throw new Exception($"RoomId {dto.RoomId} không tồn tại trong bảng Room.");

            var bookExists = await _context.Bookings.AnyAsync(b => b.BookingId == dto.BookingId);
            if(!bookExists)
                throw new Exception($"BookingId {dto.BookingId} không tồn tại trong bảng");

            var entity = new RoomSlot
            {
                SlotId = dto.SlotId,
                RoomId = dto.RoomId,
                BookingId = dto.BookingId,
                BookingDate = dto.BookingDate
            };

            return await _roomSlotRepository.CreateAsync(entity); // giả sử repository trả về số hàng ảnh hưởng
        }


        public async Task<int> UpdateAsync(RoomSlotRequest dto)
        {
            var existing = await _roomSlotRepository.GetByIdAsync(dto.RoomId, dto.SlotId);
            if (existing == null) return 0;

            existing.BookingId = dto.BookingId;
            existing.BookingDate = dto.BookingDate;

            return await _roomSlotRepository.UpdateAsync(existing);
        }

        public async Task<int> DeleteAsync(int roomId, int slotId)
        {
            return await _roomSlotRepository.DeleteAsync(roomId, slotId);
        }
    }
}
