using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using GameStore.Domain.Model;

namespace GameStore.Domain
{
    public partial class GameStoreDBContext : DbContext
    {
        public GameStoreDBContext()
            : base("name=GameStoreDBContext")
        {

        }

        public virtual DbSet<Product> Products { get; set; }
        public virtual DbSet<Category> Categories { get; set; }
    }
}
