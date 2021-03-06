/*
 * #%L
 * SciJava Common shared library for SciJava software.
 * %%
 * Copyright (C) 2009 - 2013 Board of Regents of the University of
 * Wisconsin-Madison, Broad Institute of MIT and Harvard, and Max Planck
 * Institute of Molecular Cell Biology and Genetics.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of any organization.
 * #L%
 */

package org.scijava.service;

import org.scijava.Context;
import org.scijava.event.EventService;
import org.scijava.plugin.SortablePlugin;

/**
 * Abstract superclass of {@link Service} implementations.
 * 
 * @author Curtis Rueden
 */
public abstract class AbstractService extends SortablePlugin implements
	Service
{

	/**
	 * A pointer to the service's {@link Context}. Note that for two reasons, the
	 * context is not set in the superclass:
	 * <ol>
	 * <li>As services are initialized, their dependencies are recursively created
	 * and initialized too, which is something that normal context injection does
	 * not handle. I.e., the {@link Context#inject(Object)} method assumes the
	 * context and its associated services have all been initialized already.</li>
	 * <li>Event handler methods must not be registered until after service
	 * initialization is complete (i.e., during {@link #registerEventHandlers()},
	 * after {@link #initialize()}).</li>
	 * </ol>
	 */
	private Context context;

	// -- Service methods --

	@Override
	public void initialize() {
		// NB: Do nothing by default.
	}

	@Override
	public void registerEventHandlers() {
		// TODO: Consider removing this method in scijava-common 3.0.0.
		// Instead, the ServiceHelper could just invoke the lines below directly,
		// and there would be one less boilerplate Service method to implement.
		final EventService eventService =
			getContext().getService(EventService.class);
		if (eventService != null) eventService.subscribe(this);
	}

	// -- Contextual methods --

	@Override
	public Context getContext() {
		return context;
	}

	@Override
	public void setContext(final Context context) {
		// NB: Do not call super.setContext(Context)!
		// The ServiceHelper populates service parameters.
		// We do this because we need to recursively create and initialize
		// service dependencies, rather than merely injecting existing ones.
		this.context = context;
	}

	// -- Disposable methods --

	@Override
	public void dispose() {
		// NB: Do nothing by default.
	}

	// -- Object methods --

	@Override
	public String toString() {
		return getClass().getName() + " [priority = " + getPriority() + "]";
	}

}
