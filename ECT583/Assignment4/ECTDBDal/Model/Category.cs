using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ECTDBDal.Model
{
    public partial class Category
    {
        public Category()
        {

        }

        public int CategoryId { get; set; }
        public string CategoryName { get; set; }
    }
}
