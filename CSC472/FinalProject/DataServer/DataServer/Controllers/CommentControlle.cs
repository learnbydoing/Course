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

        public IEnumerable<Comment> GetListByRestaurant(string restname)
        {
            return repo.GetListByRestaurant(restname);
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
        public Comment Create(Comment item)
        {
            return repo.Add(item);
        }

        public void Delete(int id)
        {
            repo.Remove(id);
        }
	}
}