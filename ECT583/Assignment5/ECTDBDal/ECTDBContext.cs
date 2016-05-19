using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using ECTDBDal.Model;

namespace ECTDBDal
{
    public partial class ECTDBContext : DbContext
    {
        public ECTDBContext()
            : base("name=ECTDBEntities")
        {

        }

        public virtual DbSet<Product> Products { get; set; }
        public virtual DbSet<Category> Categories { get; set; }
    }
}
