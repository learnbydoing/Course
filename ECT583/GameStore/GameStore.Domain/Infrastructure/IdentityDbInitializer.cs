using GameStore.Domain.Identity;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.EntityFramework;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GameStore.Domain.Infrastructure
{
    public class IdentityDbInitializer : DropCreateDatabaseIfModelChanges<GameStoreDBContext>
    {
        protected override void Seed(GameStoreDBContext context)
        {
            PerformInitialSetup(context);
            base.Seed(context);
        }

        public void PerformInitialSetup(GameStoreDBContext context)
        {
            GetRoles().ForEach(c => context.Roles.Add(c));
            context.SaveChanges();
            PasswordHasher hasher = new PasswordHasher();
            var user = new AppUser { UserName = "admin", Email = "admin@admin.com", PasswordHash = hasher.HashPassword("admin") };
            var role = context.Roles.Where(r => r.Name == "Admin").First();
            user.Roles.Add(new IdentityUserRole { RoleId = role.Id, UserId = user.Id });
            context.Users.Add(user);
            context.SaveChanges();
        }

        private static List<AppRole> GetRoles()
        {
            var roles = new List<AppRole> {
               new AppRole {Name="Admin", Description="Admin"},
               new AppRole {Name="Regular", Description="Regular"},
               new AppRole {Name="Advanced", Description="Advanced"}
            };

            return roles;
        }
    }
}
