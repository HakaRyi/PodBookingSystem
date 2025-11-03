using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PodBookingSystem.B.ServiceLayer.DTO.Request
{
    public class CreateDetailDto
    {
        public TimeOnly StartTime { get; set; }
        public TimeOnly EndTime { get; set; }
        public string BookingType { get; set; } // "HOURS" hoặc "DAY"
        public List<SlotDto> Slots { get; set; } = new();
    }
}
