using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;

namespace DataServer.Models
{
    //[DataContractAttribute]
    public class Comment
    {
        public Comment()
        {

        }
        public Comment(int id, String content, String restname, int userid)
        {
            this.Id = id;
            this.Content = content;
            this.RestName = restname;
            this.UserId = userid;
        }

        public int Id { get; set; }
        public string Content { get; set; }
        public String RestName { get; set; }
        public int UserId { get; set; }
    }
}