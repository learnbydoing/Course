using System.Collections.Generic;
using System.Web.Http;
using DataServer.Models;

namespace DataServer.Controllers
{
    public class CommentController : ApiController
    {
        private CommentRespository repo = CommentRespository.Current;

        public IEnumerable<Comment> GetAll()
        {
            return repo.GetList();
        }

        public IEnumerable<Comment> GetListByRestaurant(int restid)
        {
            return repo.GetListByRestaurant(restid);
        }

        public IEnumerable<Comment> GetListByUser(int id)
        {
            return repo.GetListByUser(id);
        }

        public Comment Get(int id)
        {
            return repo.Get(id);
        }

        [HttpPost]
        public HttpResult Create(CommentInfo item)
        {
            repo.Add(item);
            return new HttpResult { RetCode = 0, Message = "Succeed to submit comment." };
        }

        public void Delete(int id)
        {
            repo.Remove(id);
        }
	}
}