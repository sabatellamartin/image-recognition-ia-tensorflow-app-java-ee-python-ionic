package com.cubit.celerity.util;

import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import com.cubit.celerity.service.iservice.IRecognitionService;

/**
 *
 * This procedure call a script in the server for
 * clean old recongized images.
 * 
 * The time for schedule expresed in miliseconds,
 * for example:
 * 1 hour = 1000*60*60
 * 24 hour = 1000*60*60*24
 * ...
 * 
 */

@Startup
@Singleton
public class CleanSchedule extends TimerTask {

	@Inject
    private IRecognitionService recognitionService;
    
    private Timer timer;
    
	@PostConstruct
	private void init() {
		this.cleanCall(Constants.RECOGNITION_EXECUTE_CLEAN);
	}
	
	@PreDestroy
	private void destroy() {
		timer.cancel();
	}
	
	@Override
    public void run() {
		this.recognitionService.clean(Constants.RECOGNITION_DEACTIVATE_PERIOD, Constants.RECOGNITION_IMAGE_REPOSITORY);
    }
    
	private void cleanCall(Integer miliseconds) {
		miliseconds = miliseconds < 1000*60*60 ? 1000*60*60 : miliseconds;
		timer = new Timer();
		timer.schedule(this, 0, miliseconds);
	}

}