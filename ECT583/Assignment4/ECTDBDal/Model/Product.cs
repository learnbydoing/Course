using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ECTDBDal.Model
{
    public partial class Product
    {
        public Product()
        {

        }
        [Required]
        public int ProductId { get; set; }
        [Display(Name = "Product Name")]
        [Required]
        public string ProductName { get; set; }
        [Display(Name = "Category")]
        public int CategoryId { get; set; }        
        public double Price { get; set; }
        public string Image { get; set; }
        public string Condition { get; set; }
        public int Discount { get; set; }
    }
}
