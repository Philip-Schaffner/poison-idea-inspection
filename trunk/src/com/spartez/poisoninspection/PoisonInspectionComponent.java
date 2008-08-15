package com.spartez.poisoninspection;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.codeInspection.InspectionToolProvider;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class PoisonInspectionComponent implements ApplicationComponent, InspectionToolProvider {
	@NonNls
	@NotNull
	public String getComponentName() {
		return "PoisonInspection";
	}

	public void initComponent() {
	}

	public void disposeComponent() {
	}

	private static final Class[] INSPECTION_CLASSES = {
			SetterBypassInspection.class
	};

	public Class[] getInspectionClasses() {
		return INSPECTION_CLASSES;
	}
}
