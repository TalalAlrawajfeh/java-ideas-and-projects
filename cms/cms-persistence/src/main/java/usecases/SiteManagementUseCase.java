package usecases;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;

import beans.Site;
import persistence.SiteRepository;
import utils.EntityCopyUtil;

public class SiteManagementUseCase {
	@Autowired
	private SiteRepository siteRepository;

	public List<Site> getAllSites() {
		return StreamSupport.stream(siteRepository.findAll().spliterator(), true)
				.map(s -> EntityCopyUtil.createAndCopyFields(Site.class, s)).collect(Collectors.toList());
	}

	public List<Site> getSubSites(String uri) {
		List<Site> subSites = new ArrayList<>();
		
		siteRepository.findByUri(uri).getSubSites().stream()
				.forEach(s -> subSites.add(EntityCopyUtil.createAndCopyFields(Site.class, s)));
		
		return subSites;
	}
}
