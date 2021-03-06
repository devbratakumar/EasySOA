/**
 * EasySOA Registry
 * Copyright 2011 Open Wide
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact : easysoa-dev@googlegroups.com
 */

package org.easysoa.sca;

import java.io.File;

import org.easysoa.sca.visitors.BindingVisitorFactory;
import org.easysoa.sca.visitors.ScaVisitor;

/**
 * Introduced to ease adding another (notably FraSCAti-based) SCA importer in
 * addition to the original XML-based one.
 * 
 * @author mdutoo
 * 
 */
public interface IScaImporter
{

    /**
     * Import a SCA
     */
    public void importSCA() throws Exception;

    /**
     * 
     * @param serviceStackType
     */
    public void setServiceStackType(String serviceStackType);

    /**
     * 
     * @param serviceStackUrl
     */
    public void setServiceStackUrl(String serviceStackUrl);

    /**
     * 
     * @return
     */
    public File getCompositeFile();

    /**
     * 
     * @return
     */
    public String getServiceStackType();

    /**
     * 
     * @return
     */
    public String getServiceStackUrl();

    /**
     * 
     * @return
     */
    public String getCurrentArchiName();

    /**
     * 
     * @return
     */
    public String toCurrentArchiPath();

    

    public BindingVisitorFactory getBindingVisitorFactory();

    
    /**
     * Create and return a specific visitor for a SCA Service (including 
     * SCA ComponentService) Binding
     *  
     * @return
     *          a Service Binding visitor
     */
    public ScaVisitor createServiceBindingVisitor();


    /**
     * Create and return a specific visitor for a SCA Reference (including 
     * SCA ComponentReference) Binding
     * 
     * @return
     *          a Reference Binding visitor
     */
    public ScaVisitor createReferenceBindingVisitor();
    

    /**
     * Set the appliImplURL attribute which value is equal to the AppliImpl.URL
     * property apply to the AppliImpl.SCHEMA store of a DocumentModel object
     * (Nuxeo)
     * 
     * @param appliImplURL
     *          AppliImpl.URL property value apply to the AppliImpl.SCHEMA store
     *          of a DocumentModel
     */
    public void setAppliImplURL(String appliImplURL);
    
    /**
     * Return the appliImplURL attribute
     * 
     * @return
     *          the appliImplURL
     */
    public String getAppliImplURL();

}
