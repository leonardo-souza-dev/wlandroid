package com.leonardoserra.watchlist;

import android.content.Context;

import com.leonardoserra.watchlist.Domain.Filme;
import com.leonardoserra.watchlist.Interfaces.IRepository;
import com.leonardoserra.watchlist.Repository.CloudRepository;
import com.leonardoserra.watchlist.Repository.DeviceRepository;
import com.leonardoserra.watchlist.Repository.RepositoryIterator;

import java.util.ArrayList;

/**
 * Created by leonardo on 01/09/16.
 */
public class WLService {

    private IRepository[] repositories;
    private RepositoryIterator repositoryIterator;

    public WLService(Context context){
        DeviceRepository deviceRepository = new DeviceRepository(context);
        CloudRepository cloudRepository = new CloudRepository();

        String usuario;
        repositories = new IRepository[2];
        repositories[0] = deviceRepository;
        repositories[1] = cloudRepository;

        deviceRepository.registrarObservador(cloudRepository);

        cloudRepository.registrarObservador(deviceRepository);

        repositoryIterator = new RepositoryIterator(repositories);
    }

    public String criarOuObterUsuario(String usuario){
        boolean encontrou = false;

        while(!encontrou && repositoryIterator.hasNext()){
            IRepository iRepository = (IRepository)repositoryIterator.next();
            usuario = iRepository.criarOuObterUsuario(usuario);
            if (usuario != null){
                encontrou = true;
            }
        }
        repositoryIterator.resetPosition();
        return usuario;
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
        repositoryIterator.resetPosition();
        return filmes;
    }

    public ArrayList<Filme> buscar(String termo){
        ArrayList<Filme> filmes = null;

        boolean encontrou = false;

        while(!encontrou && repositoryIterator.hasNext()){
            IRepository iRepository = (IRepository)repositoryIterator.next();
            filmes = iRepository.buscar(termo);
            if (filmes != null){
                encontrou = true;
            }
        }
        repositoryIterator.resetPosition();
        return filmes;
    }

    public Boolean adicionarFilme(Filme filme){
        boolean sucesso = false;

        boolean encontrou = false;

        while(!encontrou && repositoryIterator.hasNext()){
            IRepository iRepository = (IRepository)repositoryIterator.next();
            sucesso = iRepository.adicionarFilme(filme);
            if (sucesso){
                encontrou = true;
            }
        }
        repositoryIterator.resetPosition();
        return sucesso;
    }

    public boolean removerFilme(Filme filme){
        boolean sucesso = false;

        boolean encontrou = false;

        while(!encontrou && repositoryIterator.hasNext()){
            IRepository iRepository = (IRepository)repositoryIterator.next();
            sucesso = iRepository.removerFilme(filme);
            if (sucesso){
                encontrou = true;
            }
        }
        repositoryIterator.resetPosition();
        return sucesso;
    }


}
