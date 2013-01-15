package de.adorsys.forge.plugins.annotation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.forge.parser.java.Annotation;
import org.jboss.forge.parser.java.JavaSource;
import org.jboss.forge.resources.java.JavaResource;
import org.jboss.forge.shell.ShellPrompt;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.Command;
import org.jboss.forge.shell.plugins.Option;
import org.jboss.forge.shell.plugins.PipeOut;
import org.jboss.forge.shell.plugins.Plugin;

/**
 *
 */
@Alias("annotation")
public class AnnotationPlugin implements Plugin {
	@Inject
	private ShellPrompt prompt;

	@Command("add-class-annotation")
	public void addClassAnnotationCommand(@Option(name = "annotationClass") String annotationClass, @Option(required = false) JavaResource[] targets,
			PipeOut out) {
		try {
			if (targets.length != 0) {
				JavaResource resource = targets[0];
				JavaSource source = resource.getJavaSource();
				removeClassAnnotations(source, annotationClass);
				source.addImport(annotationClass);
				String annotationName = annotationClass.substring(annotationClass.lastIndexOf(".") + 1);
				source.addAnnotation(annotationName);
				resource.setContents(source);
			}
		} catch (Exception e) {
			out.print(e.getMessage());
			e.printStackTrace();
		}
		out.println("FOOOOO: " + targets[0].getName());
	}

	@Command("remove-class-annotation")
	public void removeClassAnnotationCommand(@Option(name = "annotationClass") String annotationClass,
			@Option(required = false) JavaResource[] targets, PipeOut out) {
		try {
			if (targets.length != 0) {
				JavaResource resource = targets[0];
				JavaSource source = resource.getJavaSource();
				removeClassAnnotations(source, annotationClass);
				resource.setContents(source);
			}
		} catch (Exception e) {
			out.print(e.getMessage());
			e.printStackTrace();
		}
		out.println("FOOOOO: " + targets[0].getName());
	}

	private void removeClassAnnotations(JavaSource source, String annotationClass) {
		String annotationName = annotationClass.substring(annotationClass.lastIndexOf(".") + 1);

		source.removeImport(annotationClass);
		List<Annotation> toRemove = new ArrayList<Annotation>();
		for (Annotation annotation : (List<Annotation>) source.getAnnotations()) {
			if (annotation.getName().equals(annotationName)) {
				toRemove.add(annotation);
			}
		}
		for (Annotation annotation : (List<Annotation>) toRemove) {
			source.removeAnnotation(annotation);
		}
	}

}
