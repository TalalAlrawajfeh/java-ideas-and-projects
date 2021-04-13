package utils;

import org.apache.log4j.Logger;

import antlr.collections.List;
import beans.Page;
import beans.Site;
import entities.PageEntity;
import entities.SiteEntity;

public class EntityCopyUtil {
	private static final String CREATE_AND_COPY_FIELDS_ERROR = "Create And Copy Fields Error";
	private static final CopySetting[] COPY_SETTINGS = new CopySetting[] {
			new CopySetting(SiteEntity.class, Site.class), new CopySetting(PageEntity.class, Page.class),
			new CopySetting(Page.class, PageEntity.class), new CopySetting(Site.class, SiteEntity.class) };

	private static Logger logger = Logger.getLogger(EntityCopyUtil.class);

	private EntityCopyUtil() {
		/* static class */
	}

	public static <D, S> D createAndCopyFields(Class<D> destionationClass, S source) {
		try {
			D destination = destionationClass.newInstance();

			CopyUtil.copyFields(destination, source, COPY_SETTINGS, new Class<?>[] { List.class }, 3);

			return destination;
		} catch (Exception e) {
			logger.error(CREATE_AND_COPY_FIELDS_ERROR, e);

			throw new UtilException(e);
		}
	}

	public static <D, S> D createAndCopyFields(Class<D> destionationClass, S source, CopySetting[] copySettings) {
		try {
			D destination = destionationClass.newInstance();

			CopyUtil.copyFields(destination, source, copySettings, new Class<?>[] { List.class }, 3);

			return destination;
		} catch (Exception e) {
			logger.error(CREATE_AND_COPY_FIELDS_ERROR, e);

			throw new UtilException(e);
		}
	}

	public static <D, S> D createAndCopyFields(Class<D> destionationClass, S source, int depth) {
		try {
			D destination = destionationClass.newInstance();

			CopyUtil.copyFields(destination, source, COPY_SETTINGS, new Class<?>[] { List.class }, depth);

			return destination;
		} catch (Exception e) {
			logger.error(CREATE_AND_COPY_FIELDS_ERROR, e);

			throw new UtilException(e);
		}
	}
}
