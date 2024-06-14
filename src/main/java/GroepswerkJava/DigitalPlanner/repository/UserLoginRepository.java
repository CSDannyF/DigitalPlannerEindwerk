package GroepswerkJava.DigitalPlanner.repository;

import GroepswerkJava.DigitalPlanner.model.UserLogin;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserLoginRepository extends JpaRepository<UserLogin, Long>
{
    UserLogin findByUserLoginId(long userLoginId);
    UserLogin findByEmail(String email);
}
