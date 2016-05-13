/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Johnny.Dao;

import Johnny.Beans.Game;
import Johnny.Common.Constants;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author RZHUANG
 */
public class GameDao {
    private List<Game> games = new ArrayList<Game>();
    public List<Game> getGameList() {
        if (games!=null && games.size() > 0) {
            return games;
        }
        
        Game ea_fifa = new Game("ea_fifa", Constants.CONST_ELECTRONICARTS_LOWER, "FIFA 2016",59.99,"games/ea_fifa.jpg",Constants.CONST_ELECTRONICARTS,"New",10);
        Game ea_nfs = new Game("ea_nfs", Constants.CONST_ELECTRONICARTS_LOWER,"Need for Speed",59.99,"games/ea_nfs.jpg",Constants.CONST_ELECTRONICARTS,"New",10);
        games.add(ea_fifa);
        games.add(ea_nfs);
        Game activision_cod = new Game("activision_cod", Constants.CONST_ACTIVISION_LOWER, "Call Of Duty",54.99,"games/activision_cod.jpg",Constants.CONST_ACTIVISION,"New",10);
        games.add(activision_cod);
        Game tti_evolve = new Game("tti_evolve", Constants.CONST_TAKETWOINTERACTIVE_LOWER, "Evolve",49.99,"games/tti_evolve.jpg",Constants.CONST_TAKETWOINTERACTIVE,"New",10);
        games.add(tti_evolve);
        return games;        
    }    
    
    public List<Game> getGameList(String maker) {
        if (maker==null || maker.isEmpty()) {
            return getGameList();
        }
        if (games.size() == 0) {
            getGameList();
        }
        List<Game> res = new ArrayList<Game>();
        for(Game game : games) {
            if (game.getMaker().toLowerCase().equals(maker.toLowerCase())) {
                res.add(game);
            }
        }
        return res;
    }
}
