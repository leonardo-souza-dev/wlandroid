package com.leonardoserra.watchlist;

import android.content.Context;

import com.leonardoserra.watchlist.Domain.Filme;

import java.util.ArrayList;

/**
 * Created by leonardo on 01/09/16.
 */
public class WLService {

    private CloudRepository _cloudRepository;
    private IRepository[] repositories;
    private RepositoryIterator repositoryIterator;

    public WLService(Context context){
        repositories = new IRepository[2];
        repositories[0] = new DeviceRepository(context);
        repositories[1] = new CloudRepository("5f477e77-34a3-4577-becb-18f3a5070e94");
        //repositories[0] = new CloudRepository("");

        repositoryIterator = new RepositoryIterator(repositories);
    }

    public ArrayList<Filme> obterMyListt(){
        ArrayList<Filme> filmes = null;
        boolean encontrou = false;

        while(!encontrou && repositoryIterator.hasNext()){
            IRepository iRepository = (IRepository)repositoryIterator.next();
            filmes = iRepository.obterMyListt();
            if (filmes != null){
                encontrou = true;
            }
        }

        return filmes;
    }


}
