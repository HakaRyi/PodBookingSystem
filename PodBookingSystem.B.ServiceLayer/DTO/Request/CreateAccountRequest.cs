using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PodBookingSystem.B.ServiceLayer.DTO.Request
{
    public class CreateAccountRequest
    {
        public string Email { get; set; }
        public string Name { get; set; }
        public string Phone { get; set; }
    }
}
