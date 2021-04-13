package tools;

import java.util.List;

import com.tools.beans.DataImporterSettings;
import com.tools.parsers.SAXConfigurationsFileParser;
import com.tools.pools.DirectoryWatchersPool;
import com.tools.watchers.DirectoryWatcher;

public class DataImporter {
	public static void main(String[] args) {
		SAXConfigurationsFileParser xmlConfigurationsFileParser = new SAXConfigurationsFileParser();
		List<DataImporterSettings> dataImporterSettingsList = null;

		dataImporterSettingsList = xmlConfigurationsFileParser.parseConfigurationsFile(args[0]);

		if (dataImporterSettingsList == null) {
			System.out.println("Error while reading settings file");
		} else {
			importData(dataImporterSettingsList);
		}
	}

	private static void importData(List<DataImporterSettings> dataImporterSettings) {
		DirectoryWatchersPool directoryWatcherPool = new DirectoryWatchersPool(dataImporterSettings.size());

		dataImporterSettings.stream().forEach(d -> {
			DirectoryWatcher directoryWatcher;
			try {
				directoryWatcher = directoryWatcherPool.take();
				directoryWatcher.startWatching(d);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
