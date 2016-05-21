using Microsoft.AspNet.Identity.EntityFramework;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GameStore.Domain.Identity
{
    public class AppRole : IdentityRole
    {
        public AppRole() : base() { }

        public AppRole(string description)
            : base()
        {
            this.Description = description;
        }

        public virtual string Description { get; set; }
    }
}
