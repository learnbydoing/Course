using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DataServer.Models
{
    public class CommentRespository
    {
        private static CommentRespository repo = new CommentRespository();

        public static CommentRespository Current
        {
            get
            {
                return repo;
            }
        }

        private List<Comment> comments = new List<Comment> {
           new Comment(1, 
                    "The food and the service was excellent as well as unique. ",
                    "Bavette's Bar and Boeuf(Server)",
                    1),
            new Comment(2,
                    "Completely changed how I think about food.",
                    "Swirlz Cupcakes(Server)",
                    1),
            new Comment(3,
                    "I thoroughly enjoyed my meal.",
                    "Jeni's Splendid Ice Creams(Server)",
                    1),
            new Comment(4,
                    "The service was great.",
                    "Intelligentsia Coffee",
                    1),
            new Comment(5,
                    "The overall food itself was probably better than Fat Duck",
                    "Firecakes Donuts",
                    1),
            new Comment(6,
                    "good food",
                    "Polo Cafe & Catering Bridgeport",
                    1),
            new Comment(7,
                    "Easily one of the best meals of my life!",
                    "Garrett Popcorn Shops",
                    1),
            new Comment(8,
                    "We were happily seated at our table and the courses begins.",
                    "Lickity Split Frozen Custard & Sweets",
                    1),
            new Comment(9,
                    "The service was impeccable and the whole experience was definitely to remember for a lifetime!",
                    "Wildberry Pancakes and Cafe",
                    2),
            new Comment(10,
                    "Found flavors and service lacking compared to other Michelin starred restaurants.",
                    "Doughnut Vault",
                    2),
            new Comment(11,
                    "Definitely met but did not exceed expectations.",
                    "Glazed and Infused",
                    2),
            new Comment(12,
                    "It is an extraordinary experience for sure",
                    "Alinea",
                    2),
            new Comment(13,
                    "The actual dining room is elegant but still simplistic.",
                    "Ghirardelli Ice Cream & Chocolate Shop",
                    3),
            new Comment(14,
                    "One of the most impressive dishes was also the one that made me the saddest though.",
                    "Do - Rite Donuts",
                    3),
            new Comment(15,
                    "What a treat! This is an experience that everyone should be able to do once. I",
                    "West Egg Cafe",
                    3),
        };

        public IEnumerable<Comment> GetList()
        {
            return comments;
        }

        public IEnumerable<Comment> GetListByRestaurant(string restname)
        {
            return comments.Where(r => r.RestName.ToLower().Equals(restname.ToLower()));
        }

        public IEnumerable<Comment> GetListByUser(int userid)
        {
            return comments.Where(r => r.UserId == userid);
        }

        public Comment Get(int id)
        {
            return comments.Where(r => r.UserId.Equals(id)).FirstOrDefault();
        }

        public Comment Add(Comment item)
        {
            comments.Add(item);
            return item;
        }

        public void Remove(int id)
        {
            Comment item = Get(id);
            if (item != null)
            {
                comments.Remove(item);
            }
        }        
    }
}