using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using PodBookingSystem.B.ServiceLayer;
using PodBookingSystem.C.RepositoryLayer.Models;

namespace PodBookingSystem.A.WebAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class FeedbackController : ControllerBase
    {
        private readonly ILogger<FeedbackController> _logger;
        private readonly FeedbackService _feedbackService;
        public FeedbackController(ILogger<FeedbackController> logger, FeedbackService feedbackService)
        {
            _logger = logger;
            _feedbackService = feedbackService;
        }

        [HttpGet]
        public async Task<IActionResult> GetAllFeedbacks()
        {
            var feedbacks = await _feedbackService.GetAllFeedbacks();
            return Ok(feedbacks);
        }

        // ✅ GET: api/feedback/{id}
        [HttpGet("{id}")]
        public async Task<IActionResult> GetFeedbackById(int id)
        {
            var feedback = await _feedbackService.GetFeedbackById(id);
            if (feedback == null)
            {
                _logger.LogWarning($"Feedback with id {id} not found.");
                return NotFound(new { message = "Feedback not found" });
            }
            return Ok(feedback);
        }

        // ✅ GET: api/feedback/booking/{bookingId}
        [HttpGet("booking/{bookingId}")]
        public IActionResult GetByBooking(int bookingId)
        {
            var feedback = _feedbackService.GetByBooking(bookingId);
            if (feedback == null)
            {
                _logger.LogWarning($"Feedback for booking {bookingId} not found.");
                return NotFound(new { message = "Feedback not found for this booking" });
            }
            return Ok(feedback);
        }

        // ✅ POST: api/feedback
        [HttpPost]
        public async Task<IActionResult> AddFeedback([FromBody] Feedback feedback)
        {
            if (feedback == null)
                return BadRequest(new { message = "Invalid feedback data" });

            var result = await _feedbackService.AddFeedback(feedback);
            if (result > 0)
            {
                _logger.LogInformation($"Feedback created with ID {feedback.FeedbackId}");
                return CreatedAtAction(nameof(GetFeedbackById), new { id = feedback.FeedbackId }, feedback);
            }
            return StatusCode(500, new { message = "Failed to add feedback" });
        }

        // ✅ PUT: api/feedback/{id}
        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateFeedback(int id, [FromBody] Feedback feedback)
        {
            if (feedback == null || feedback.FeedbackId != id)
                return BadRequest(new { message = "Feedback ID mismatch" });

            var existingFeedback = await _feedbackService.GetFeedbackById(id);
            if (existingFeedback == null)
                return NotFound(new { message = "Feedback not found" });

            var result = await _feedbackService.UpdateFeedback(feedback);
            if (result > 0)
            {
                _logger.LogInformation($"Feedback {id} updated successfully.");
                return Ok(new { message = "Feedback updated successfully" });
            }

            return StatusCode(500, new { message = "Failed to update feedback" });
        }

        // ✅ DELETE: api/feedback/{id}
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteFeedback(int id)
        {
            var existingFeedback = await _feedbackService.GetFeedbackById(id);
            if (existingFeedback == null)
                return NotFound(new { message = "Feedback not found" });

            var result = await _feedbackService.DeleteFeedback(id);
            if (result > 0)
            {
                _logger.LogInformation($"Feedback {id} deleted successfully.");
                return Ok(new { message = "Feedback deleted successfully" });
            }

            return StatusCode(500, new { message = "Failed to delete feedback" });
        }
    }
}
