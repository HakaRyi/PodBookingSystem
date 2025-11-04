using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;
using Microsoft.IdentityModel.Tokens;
using PodBookingSystem.B.ServiceLayer.DTO.Request;
using PodBookingSystem.C.RepositoryLayer.Models;
using PodBookingSystem.C.RepositoryLayer.UnitOfWorks;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

namespace PodBookingSystem.B.ServiceLayer
{
    public class AccountService
    {
        private readonly IUnitOfWork _unitOfWork;
        private readonly ILogger<AccountService> _logger;
        private readonly IConfiguration _configuration;
        public AccountService(IUnitOfWork unitOfWork, ILogger<AccountService> logger, IConfiguration configuration)
        {
            _unitOfWork = unitOfWork;
            _logger = logger;
            _configuration = configuration;
        }

        public async Task<List<Account>> GetAllAccountsAsync()
        {
            try
            {
                return await _unitOfWork.AccountRepository.GetAllAsync();
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Lỗi khi lấy danh sách tài khoản");
                throw new Exception("Không thể lấy danh sách tài khoản. Vui lòng thử lại sau.", ex);
            }
        }

        // Lấy Account theo ID
        public async Task<Account?> GetAccountByIdAsync(int id)
        {
            try
            {
                return await _unitOfWork.AccountRepository.GetById(id);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, $"Lỗi khi lấy tài khoản ID = {id}");
                throw new Exception("Không thể lấy thông tin tài khoản.", ex);
            }
        }

        // Lấy Account theo Email
        public async Task<Account?> GetAccountByEmailAsync(string email)
        {
            try
            {
                if (string.IsNullOrEmpty(email))
                    throw new ArgumentException("Email không được để trống.");

                return await _unitOfWork.AccountRepository.GetByEmail(email);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, $"Lỗi khi lấy tài khoản theo email = {email}");
                throw new Exception("Không thể lấy tài khoản theo email.", ex);
            }
        }

        // Tạo mới Account
        public async Task<bool> CreateAccountAsync(Account account)
        {
            try
            {
                if (account == null)
                    throw new ArgumentNullException(nameof(account));

                var result = await _unitOfWork.AccountRepository.CreateAsync(account);
                return result > 0;
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Lỗi khi tạo mới tài khoản");
                throw new Exception("Không thể tạo mới tài khoản.", ex);
            }
        }

        // Cập nhật Account
        public async Task<bool> UpdateAccountAsync(Account account)
        {
            try
            {
                if (account == null)
                    throw new ArgumentNullException(nameof(account));

                var result = await _unitOfWork.AccountRepository.UpdateAsync(account);
                return result > 0;
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, $"Lỗi khi cập nhật tài khoản ID = {account?.AccId}");
                throw new Exception("Không thể cập nhật tài khoản.", ex);
            }
        }

        // Xóa Account
        public async Task<bool> DeleteAccountAsync(int id)
        {
            try
            {
                var result = await _unitOfWork.AccountRepository.DeleteAsync(id);
                return result > 0;
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, $"Lỗi khi xóa tài khoản ID = {id}");
                throw new Exception("Không thể xóa tài khoản.", ex);
            }
        }

        public string GenerateJwtToken(Account user)
        {
            var claims = new[]
            {
                new Claim("accountId", user.AccId.ToString()),
                new Claim("accountEmail", user.Email),
                new Claim("roleId", user.RoleId.ToString())
            };

            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration["JwtSettings:SecretKey"]));
            var creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);

            var token = new JwtSecurityToken(
                issuer: _configuration["JwtSettings:Issuer"],
                audience: _configuration["JwtSettings:Audience"],
                claims: claims,
                expires: DateTime.UtcNow.AddHours(2),
                signingCredentials: creds);

            return new JwtSecurityTokenHandler().WriteToken(token);
        }
        public async Task<bool> UpdateProfileAsync(int accountId, UpdateProfileRequest dto)
        {
            try
            {
                var account = await _unitOfWork.AccountRepository.GetById(accountId);
                if (account == null)
                    throw new Exception($"Không tìm thấy tài khoản có ID = {accountId}");

                // Chỉ cập nhật các trường thuộc profile
                if (!string.IsNullOrEmpty(dto.Name))
                    account.Name = dto.Name;

                if (!string.IsNullOrEmpty(dto.Phone))
                    account.Phone = dto.Phone;

                if (!string.IsNullOrEmpty(dto.Email))
                    account.Email = dto.Email;

                if (!string.IsNullOrEmpty(dto.AvatarUrl))
                    account.AvatarUrl = dto.AvatarUrl;

                var result = await _unitOfWork.AccountRepository.UpdateAsync(account);
                return result > 0;
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, $"Lỗi khi cập nhật profile cho account ID = {accountId}");
                throw new Exception("Không thể cập nhật profile tài khoản.", ex);
            }
        }
    }
}
