using ECTDBDal.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Assignment4.Models
{
    public class CategoryEditViewModel
    {
        public String Title { get; set; }
        public Category EditableCategory { get; set; }
        public String StatusMessage { get; set; }
        public bool IsErrorStatusMessage { get; set; }
    }
}