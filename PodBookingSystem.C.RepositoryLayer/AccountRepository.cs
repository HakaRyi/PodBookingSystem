using Microsoft.EntityFrameworkCore;
using PodBookingSystem.C.RepositoryLayer.DBContext;
using PodBookingSystem.C.RepositoryLayer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PodBookingSystem.C.RepositoryLayer
{
    public class AccountRepository
    {
        private readonly PodBookingSystemContext _context;
        public AccountRepository(PodBookingSystemContext context) => _context = context;

        public async Task<List<Account>> GetAllAsync()
        {
            return await _context.Accounts.ToListAsync();
        }

        public async Task<Account> GetById(int id)
        {
            return await _context.Accounts.FirstOrDefaultAsync(a => a.AccId == id);
        }

        public async Task<Account> GetByEmail(string email)
        {
            return await _context.Accounts.FirstOrDefaultAsync(a => a.Email.Equals(email));
        }

        public async Task<int> CreateAsync(Account account)
        {
            var result = await _context.Accounts.AddAsync(account);
            return await _context.SaveChangesAsync();
        }

        public async Task<int> UpdateAsync(Account account)
        {
            _context.Accounts.Update(account);
            return await _context.SaveChangesAsync();
        }

        public async Task<int> DeleteAsync(int id)
        {
            var account = await GetById(id);
            if (account != null)
            {
                _context.Accounts.Remove(account);
                return await _context.SaveChangesAsync();
            }
            return 0;
        }

    }
}
