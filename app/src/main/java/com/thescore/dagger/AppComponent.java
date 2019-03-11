package com.thescore.dagger;

import com.thescore.home.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
		AppContextModule.class
})
public interface AppComponent {

	void inject(MainActivity target);

}
