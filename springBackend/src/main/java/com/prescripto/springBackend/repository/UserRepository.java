package com.prescripto.springBackend;

import com.prescripto.springBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
//    can defines custom method
}
