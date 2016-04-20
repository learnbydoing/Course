using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Assignment3.Models
{
    public class CustomerEditViewModel
    {
        public String Title { get; set; }
        public Customer EditableCustomer { get; set; }
        public String Message { get; set; }
        public bool IsError { get; set; }
    }
}