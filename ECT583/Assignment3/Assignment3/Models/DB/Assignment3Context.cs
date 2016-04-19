using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data.Entity;

namespace Assignment3.Models
{
    public class Assignment3Context: DbContext
    {
        public Assignment3Context()
            : base("name=Assignment3Context")
        {
            Database.SetInitializer<Assignment3Context>(new DropCreateDatabaseIfModelChanges<Assignment3Context>());
        }


        public virtual DbSet<Customer> Customers { get; set; }
    }
}