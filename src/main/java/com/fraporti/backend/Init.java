package com.fraporti.backend;

import com.fraporti.backend.model.Permissao;
import com.fraporti.backend.repository.PermissaoRepository;
import org.springframework.stereotype.Service;

@Service
public class Init {
    /**
     * Só para testes
     */
    public Init(PermissaoRepository repo){
        Permissao user = new Permissao();
        Permissao admin = new Permissao();
        user.setDescription("USER");
        admin.setDescription("ADMIN");
        repo.save(admin);
        repo.save(user);
    }
}
