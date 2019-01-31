package kr.co.inslab.codealley.delivery.service;

import kr.co.inslab.codealley.delivery.model.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * DB에 CRUD 작업을 수행하는 서비스 클래스
 */
@Repository("DataService")
public class DataService {

	private Log log = LogFactory.getLog(DataService.class);

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * 추가된 application 정보를 삭제하는 함수
	 * @param domain 그룹 이름
	 * @param type Application 종류
	 * @param name Application 이름
	 */
	@Transactional(rollbackFor = {SQLException.class}, propagation = Propagation.REQUIRES_NEW)
	public void deleteConfigurationData(String domain, String type, String name) {
		log.info(String.format("Delete application info : %s named %s of %s", type, name, domain));
		Query query = entityManager.createQuery("DELETE FROM ApplicationsVO WHERE name=:name");
		query.setParameter("name", name);
		query.executeUpdate();
	}

	/**
	 * Application 상태값을 갱신하는 함수
	 * @param domain 그룹 이름
	 * @param type Application 종류
	 * @param name Application 이름
	 * @param status 변경할 상태값
	 * @return int 변경된 경우 1, 변경된 항목이 없으면 0
	 */
	@Transactional(rollbackFor = {SQLException.class}, propagation = Propagation.REQUIRES_NEW)
	public int updateStatus(String domain, String type, String name, String status) {
		log.info(String.format("Update application status of %s. Domain:%s Name:%s", type, domain, name));
		Query query = entityManager.createQuery("UPDATE ApplicationsVO SET status=:status WHERE name=:name");
		query.setParameter("status", status);
		query.setParameter("name", name);
		return query.executeUpdate();
	}

	/**
	 * Application의 healthy 정보 갱신하는 함수
	 * @param domain 그룹 이름
	 * @param type Application 종류
	 * @param name Application 이름
	 * @param healthy 갱신할 healthy 정보
	 * @return
	 */
	@Transactional(rollbackFor = {SQLException.class}, propagation = Propagation.REQUIRES_NEW)
	public int updateHealthy(String domain, String type, String name, int healthy) {
		log.info(String.format("Update application healthy of %s. Domain:%s Name:%s", type, domain, name));
		Query query = entityManager.createQuery("UPDATE ApplicationsVO SET healthy=:healthy WHERE name=:name");
		query.setParameter("healthy", healthy);
		query.setParameter("name", name);
		return query.executeUpdate();
	}

	/**
	 * Application의 service port 번호를 갱신하는 함수
	 * @param domain 그룹 이름
	 * @param type Application 종류
	 * @param name Application 이름
	 * @param ports 갱신할 port 맵
	 */
	@Transactional(rollbackFor = {SQLException.class}, propagation = Propagation.REQUIRES_NEW)
	public void updatePort(String domain, String type, String name, Map<String, Integer> ports) {
		log.info(String.format("Update application port of %s. Domain:%s Name:%s", type, domain, name));
		Query query = entityManager.createQuery("UPDATE ApplicationsVO SET " +
				"tcp_port=:tcp_port, http_port=:http_port, https_port=:https_port, " +
				"git_port=:git_port, ssh_port=:ssh_port WHERE name=:name");
		query.setParameter("tcp_port", ports.get("tcp_port"));
		query.setParameter("http_port", ports.get("http_port"));
		query.setParameter("https_port", ports.get("https_port"));
		query.setParameter("git_port", ports.get("git_port"));
		query.setParameter("ssh_port", ports.get("ssh_port"));
		query.setParameter("name", name);
		query.executeUpdate();
	}

	/**
	 * Application의 token 값을 갱신하는 함수
	 * @param domain 그룹 이름
	 * @param type Application 종류
	 * @param name Application 이름
	 * @param token 갱신할 token 값
	 * @return int 갱신되었으면 1, 변경사항 없다면 0
	 */
	@Transactional(rollbackFor = {SQLException.class}, propagation = Propagation.REQUIRES_NEW)
	public int updateToken(String domain, String type, String name, String token) {
		log.info(String.format("Update token of %s. Domain:%s Name:%s", type, domain, name));
		Query query = entityManager.createQuery("UPDATE ApplicationsVO SET token=:token WHERE name=:name");
		query.setParameter("token", token);
		query.setParameter("name", name);

		return query.executeUpdate();
	}

	/**
	 * 설치된 모든 application의 정보를 조회하는 함수
	 * @param domain 그룹 이름
	 * @return String Application 들의 정보
	 */
	public String selectAllApplicationsInfo(String domain) {
		List<ApplicationsVO> applications = new ArrayList<ApplicationsVO>();

		try {
			log.info("Application domain : " + domain);
			TypedQuery<ApplicationsVO> qry = entityManager.createQuery("SELECT A FROM ApplicationsVO A WHERE domain=:domain", ApplicationsVO.class);
			qry.setParameter("domain", domain);
			applications = qry.getResultList();
		} catch (Exception e) {
			if(e instanceof NoResultException) {
				log.error(e.getMessage());
			} else {
				log.error(e.getMessage(), e);
			}
		}

		return applications.toString();
	}

	/**
	 * Application의 정보를 조회하는 함수
	 * @param groupId 그룹 이름
	 * @param appId Application 정보
	 * @param name Application 이름
	 * @return String Application의 정보
	 */
	public String selectApplication(String groupId, String appId, String name) {
		String app = null;

		try {
			log.info("DB Name : " + name);
			TypedQuery<ApplicationsVO> qry = entityManager.createQuery("SELECT A FROM ApplicationsVO A WHERE name=:name", ApplicationsVO.class);
			qry.setParameter("name", name);
			ApplicationsVO application = qry.getSingleResult();
			if(application != null) app = application.toString();
		} catch (Exception e) {
			if(e instanceof NoResultException) {
				log.error(e.getMessage());
			} else {
				log.error(e.getMessage(), e);
			}
		}

		return app;
	}

	/**
	 * Application의 status 값을 조회하는 함수
	 * @param groupId 그룹 이름
	 * @param appId Application 종류
	 * @param name Application 이름
	 * @return String 조회된 상태값 (없다면 null)
	 */
	public String selectApplicationStatus(String groupId, String appId, String name) {
		String status = null;
		try {
			TypedQuery<ApplicationsVO> qry = entityManager.createQuery("SELECT A FROM ApplicationsVO A WHERE name=:name", ApplicationsVO.class);
			qry.setParameter("name", name);
			ApplicationsVO result = qry.getSingleResult();
			if (result != null) status = result.getStatus();
		} catch (Exception e) {
			if(e instanceof NoResultException) {
				log.error(e.getMessage());
			} else {
				log.error(e.getMessage(), e);
			}
		}

		return status;
	}

	/**
	 * 그룹 이름(sub domain)을 조회하는 함수
	 * @param name 조회할 그룹 이름
	 * @return DomainVO 조회된 도메인 정보
	 */
	@Transactional(readOnly = true)
	public DomainVO selectDomain(String name) { 
		DomainVO result = null; 

		try { 
			log.info("Domain Name : " + name);
			TypedQuery<DomainVO> qry = entityManager.createQuery("SELECT A FROM DomainVO A WHERE A.domain=:domain", DomainVO.class);
			qry.setParameter("domain", name); 
			
			result = qry.getSingleResult();
		} catch (Exception e) {
			if(e instanceof NoResultException) {
				log.error(e.getMessage());
			} else {
				log.error(e.getMessage(), e);
			}
		}

		return result;
	}

	/**
	 * 그룹을 추가하는 함수
	 * @param groupId 추가할 그룹 이름
	 * @param userId 추가하는 사용자의 ID
	 */
	public void insertDomain(String groupId, String userId) {
		DomainVO vo = new DomainVO();
		vo.setUserId(userId);
		vo.setDomain(groupId);
		vo.setCreatedAt(new Timestamp((new Date()).getTime()));
		
		log.info("Insert Domain Info : " + vo.toString());
		entityManager.persist(vo);
	}

	/**
	 * 그룹과 별개로 권한 정보 등을 가지고 있는 도메인 그룹을 추가하는 함수
	 * @param groupId 추가할 그룹 이름
	 * @param userId 추가하는 사용자의 ID
	 */
	public void insertDomainGroup(String groupId, String userId) {
		DomainGroupVO groupVO = new DomainGroupVO();
		groupVO.setUserId(userId);
		groupVO.setDomain(groupId);
		groupVO.setRole("owner");
		groupVO.setPermCreate(1);
		groupVO.setPermRead(1);
		groupVO.setPermUpdate(1);
		groupVO.setPermDelete(1);

		log.info("Insert default domain owner to domain_group : " + groupVO.toString());
		entityManager.persist(groupVO);
	}

	/**
	 * 그룹 (domain)을 삭제하는 함수
	 * @param groupId 삭제할 그룹 이름
	 */
	public void deleteDomain(String groupId) {
		DomainVO vo = selectDomain(groupId);
		
		if(vo != null) {
			log.info("Delete Domain Info : " + vo.toString());
			DomainVO domainVO= entityManager.merge(vo); 
			entityManager.remove(domainVO);
		}
	}

	public SlaveVO selectSlave(String internalIp) {
		SlaveVO vo = null;
	
		try {
			TypedQuery<SlaveVO> query = entityManager.createQuery("SELECT A FROM SlaveVO A WHERE A.internal_ip =: internalIp", SlaveVO.class);
			query.setParameter("internalIp", internalIp);
			vo = query.getSingleResult();
		} catch(Exception e) {
			if(e instanceof NoResultException) {
				log.error(e.getMessage());
			} else {
				log.error(e.getMessage(), e);
			}
		}
		
		return vo;
	}

	/**
	 * Application 정보를 추가하는 함수
	 * @param domain 그룹 이름
	 * @param type Application 종류
	 * @param name Application 이름
	 * @param dataPath 파일들이 저장되는 경로
	 * @param description Application 요약정보
	 * @param cores Application에 할당될 cpu core
	 * @param memory Application에 할당될 memory
	 * @param params Application에 필요한 기타 정보
	 */
	public void insertApplication(String domain, String type, String name, String dataPath,
								  String description, Double cores, Integer memory,
								  Map<String, String> params) {
		ApplicationsVO vo = new ApplicationsVO();
		vo.setDomain(domain);
		vo.setType(type);
		vo.setName(name);
		vo.setDataPath(dataPath);
		vo.setDescription(description);
		vo.setDbId(params.get("username"));
		vo.setDbPassword(params.get("password"));
		vo.setCores(cores);
		vo.setMemory(memory);
		vo.setCreatedAt(new Timestamp((new Date()).getTime()));

		log.info("Insert application info : " + vo.toString());
		entityManager.persist(vo);
	}

	/**
	 * 여러개의 VM을 조회하는 함수
	 * @param userId 생성한 사용자의 ID
	 * @return List VM list
	 */
	@Transactional(readOnly = true)
	public List<VirtualMachineVO> selectVirtualMachines(String userId) {
		List<VirtualMachineVO> result = null;

		try {  
			log.info("VM's user id : " + userId);
			TypedQuery<VirtualMachineVO> qry = entityManager.createQuery("SELECT A FROM VirtualMachineVO A WHERE A.userId = :user_id", VirtualMachineVO.class);
			qry.setParameter("user_id", userId);
			result = qry.getResultList();
		} catch (Exception e) {
			if(e instanceof NoResultException) {
				log.error(e.getMessage());
			} else {
				log.error(e.getMessage(), e);
			}
		}

		return result;
	}

	/**
	 * VM을 조회하는 함수
	 * @param name 조회할 VM의 이름
	 * @return VirtualMachineVO VM 정보
	 */
	@Transactional(readOnly = true)
	public VirtualMachineVO selectVirtualMachine(String name) {
		VirtualMachineVO vo = null;

		try {  
			log.info("VM's name : " + name);
			TypedQuery<VirtualMachineVO> qry = entityManager.createQuery("SELECT A FROM VirtualMachineVO A WHERE A.vmName = :vm_name", VirtualMachineVO.class);
			qry.setParameter("vm_name", name);
			vo = qry.getSingleResult();
		} catch (Exception e) {
			if(e instanceof NoResultException) {
				log.error(e.getMessage());
			} else {
				log.error(e.getMessage(), e);
			}
		}

		return vo;
	}

	/**
	 * VM 정보를 삭제하는 함수
	 * @param name 삭제할 VM의 이름
	 * @return int 삭제된 경우 1, 변경사항 없다면 0
	 */
	public int deleteVirtualMachine(String name) {
		VirtualMachineVO vo = selectVirtualMachine(name);
		int entities = 0;
		if(vo != null) {
			log.info("Delete Virtual Machine Info : " + vo.toString());
			Query query = entityManager.createQuery("DELETE FROM VirtualMachineVO WHERE id=:id");
			query.setParameter("id", vo.getId());
			
			entities = query.executeUpdate();
		}
		
		return entities;
	}

	/**
	 * VM 정보를 추가하는 함수
	 * @param vo 추가할 VM의 정보
	 */
	public void insertVirtualMachine(VirtualMachineVO vo) {
		log.info("Insert Virtual Machine Info : " + vo.toString());
		entityManager.persist(vo);
	}
}
