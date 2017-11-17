package ouc.cs.course.java.musicserver.service;

import java.sql.SQLException;

import ouc.cs.course.java.musicserver.dao.MusicDao;
import ouc.cs.course.java.musicserver.dao.MusicSheetDao;
import ouc.cs.course.java.musicserver.dao.MusicSheetToMusicDao;
import ouc.cs.course.java.musicserver.dao.impl.MusicDaoImpl;
import ouc.cs.course.java.musicserver.dao.impl.MusicSheetDaoImpl;
import ouc.cs.course.java.musicserver.dao.impl.MusicSheetToMusicDaoImpl;
import ouc.cs.course.java.musicserver.model.Music;
import ouc.cs.course.java.musicserver.model.MusicSheet;
import ouc.cs.course.java.musicserver.model.MusicSheetToMusic;

public class MusicSheetService {

	MusicSheetDao musicSheetDao = new MusicSheetDaoImpl();
	MusicDao musicDao = new MusicDaoImpl();
	MusicSheetToMusicDao mstmDao = new MusicSheetToMusicDaoImpl();

	public MusicSheetService() {
	}

	public int create(MusicSheet ms) throws SQLException {
		return musicSheetDao.insert(ms);
	}

	public void createOrUpdate(MusicSheet ms) throws SQLException {

		MusicSheet musicSheet = musicSheetDao.findByUuid(ms.getUuid());

		Music mu = null;
		MusicSheetToMusic mstm = null;
		int musicSheetId, musicId;

		if (musicSheet == null) {
			musicSheetId = musicSheetDao.insert(ms);

			for (String key : ms.getMusicItems().keySet()) {
				mu = musicDao.findByMd5value(key);

				if (mu == null) {
					mu = new Music(key, ms.getMusicItems().get(key));
					musicId = musicDao.insert(mu);

				} else {
					mu.setName(ms.getMusicItems().get(key));
					musicDao.update(mu);
					musicId = mu.getId();

				}

				mstm = new MusicSheetToMusic(musicSheetId, musicId);
				mstmDao.insert(mstm);
			}

		} else {
			musicSheetDao.update(ms);
			musicSheetId = musicSheet.getId();

			musicSheet.getMusicItems();

			for (String key : ms.getMusicItems().keySet()) {
				mu = musicDao.findByMd5value(key);

				if (mu == null) {
					mu = new Music(key, ms.getMusicItems().get(key));
					musicId = musicDao.insert(mu);
					mstm = new MusicSheetToMusic(musicSheetId, musicId);
					mstmDao.insert(mstm);

				} else {
					mu.setName(ms.getMusicItems().get(key));
					musicDao.update(mu);

				}
			}
		}
	}

	public void getAll() throws SQLException {
		System.out.println(musicSheetDao.findAll());
	}

	public void delete(String uuid) throws SQLException {
		musicSheetDao.deleteByUuid(uuid);
	}
}
