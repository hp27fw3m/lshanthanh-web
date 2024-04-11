/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package nb.hanquoc.web.repository;


import nb.hanquoc.core.entity.RenunciationOfCitizenship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RenunciationOfCitizenshipRepository extends JpaRepository<RenunciationOfCitizenship, Long> {
}

