using PodBookingSystem.C.RepositoryLayer.Models;
using PodBookingSystem.C.RepositoryLayer;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PodBookingSystem.B.ServiceLayer.DTO.Response;
using PodBookingSystem.B.ServiceLayer.DTO.Request;

namespace PodBookingSystem.B.ServiceLayer
{
    public class SlotService
    {
        private readonly SlotRepository _slotRepository;
        private readonly RoomSlotRepository _roomSlotRepository;

        public SlotService(SlotRepository slotRepository, RoomSlotRepository roomSlotRepository)
        {
            _slotRepository = slotRepository;
            _roomSlotRepository = roomSlotRepository;
        }

        public async Task<List<SlotResponse>> GetAllAsync()
        {
            var slots = await _slotRepository.GetAllAsync();
            return slots.Select(s => new SlotResponse
            {
                SlotId = s.SlotId,
                Description = s.Description
            }).ToList();
        }
        public async Task<List<Slot>> GetAvailableSlotsAsync(int roomId, DateOnly bookingDate)
        {
            var allSlots = await _slotRepository.GetAllAsync();
            var bookedSlotIds = await _roomSlotRepository.GetBookedSlotIdsAsync(roomId, bookingDate);

            return allSlots
                .Where(s => !bookedSlotIds.Contains(s.SlotId))
                .OrderBy(s => s.SlotId)
                .ToList();
        }
        public async Task<SlotResponse> GetByIdAsync(int id)
        {
            var slot = await _slotRepository.GetByIdAsync(id);
            if (slot == null) return null;

            return new SlotResponse
            {
                SlotId = slot.SlotId,
                Description = slot.Description
            };
        }

        public async Task<int> CreateAsync(SlotRequest dto)
        {
            var slot = new Slot
            {
                Description = dto.Description
            };
            return await _slotRepository.CreateAsync(slot);
        }

        public async Task<int> UpdateAsync(int id, SlotRequest dto)
        {
            var existing = await _slotRepository.GetByIdAsync(id);
            if (existing == null) return 0;

            existing.Description = dto.Description;
            return await _slotRepository.UpdateAsync(existing);
        }

        public async Task<int> DeleteAsync(int id)
        {
            return await _slotRepository.DeleteAsync(id);
        }
    }
}
