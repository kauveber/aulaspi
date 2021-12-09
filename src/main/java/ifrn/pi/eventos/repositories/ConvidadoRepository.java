package ifrn.pi.eventos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ifrn.pi.eventos.models.Convidado;

public interface ConvidadoRepository extends JpaRepository<Convidado, Long>{

}
