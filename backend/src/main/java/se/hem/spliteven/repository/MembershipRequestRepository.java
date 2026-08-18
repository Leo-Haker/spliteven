package se.hem.spliteven.repository;

import se.hem.spliteven.model.MembershipRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MembershipRequestRepository extends JpaRepository<MembershipRequest, Long> {
    List<MembershipRequest> findByPersonIdAndStatus(Long personId, MembershipRequest.Status status);

}
