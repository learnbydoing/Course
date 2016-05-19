using ECTDBDal.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Assignment5.Models
{
    public class CategoryEditViewModel: Category
    {
        public String Title { get; set; }
        public String StatusMessage { get; set; }
        public bool IsErrorStatusMessage { get; set; }
    }
}