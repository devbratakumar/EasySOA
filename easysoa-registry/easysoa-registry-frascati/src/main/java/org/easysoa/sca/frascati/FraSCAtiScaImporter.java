package org.easysoa.sca.frascati;

import java.io.File;

import org.easysoa.registry.frascati.FraSCAtiService;
import org.easysoa.sca.visitors.ApiBindingVisitorFactory;
import org.easysoa.sca.visitors.BindingVisitorFactory;
import org.easysoa.sca.visitors.ReferenceBindingVisitor;
import org.easysoa.sca.visitors.ScaVisitor;
import org.easysoa.sca.visitors.ServiceBindingVisitor;
import org.easysoa.services.DiscoveryService;
import org.easysoa.services.DocumentService;
import org.eclipse.stp.sca.Composite;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.runtime.api.Framework;

/**
 * Sca Importer : Uses Nuxeo API's to register services
 * @author jguillemotte
 *
 */
public class FraSCAtiScaImporter extends AbstractScaImporterBase {

	private DocumentModel parentAppliImplModel;	
	private CoreSession documentManager;
	
	/**
	 * 
	 * @param documentManager
	 * @param compositeFile
	 * @throws Exception 
	 * @throws ClientException 
	 */
	//public FraSCAtiScaImporter(CoreSession documentManager, File compositeFile) throws ClientException, Exception{
	public FraSCAtiScaImporter(BindingVisitorFactory bindingVisitorFactory, File compositeFile) throws ClientException, Exception{
		super(bindingVisitorFactory, compositeFile);
		this.documentManager = (CoreSession) bindingVisitorFactory.getDocumentManager();
		if(documentManager != null){
			parentAppliImplModel = Framework.getRuntime().getService(DocumentService.class).getDefaultAppliImpl(documentManager);
		}
		init();
	}
	
	/**
	 * Initialization
	 * @throws Exception
	 */
	private void init() throws Exception{
		frascatiService = Framework.getService(FraSCAtiService.class);
	}	
	
	@Override
    public ScaVisitor createServiceBindingVisitor() {
        //return new ServiceBindingVisitor(this, Framework.getRuntime().getService(DiscoveryService.class));
		return this.bindingVisitorFactory.createServiceBindingVisitor(this);
    }
    
	@Override
    public ScaVisitor createReferenceBindingVisitor() {
    	//return new ReferenceBindingVisitor(this, Framework.getRuntime().getService(DiscoveryService.class));
		return this.bindingVisitorFactory.createReferenceBindingVisitor(this);
    }
    
    @Override
    public void visitComposite(Composite composite){
    	super.visitComposite(composite);
    }

	@Override
	public String getModelProperty(String arg0, String arg1) throws Exception {
		return (String)(parentAppliImplModel.getProperty(arg0, arg1));
	}

	/*@Override
	public Object getDocumentManager() {
		return documentManager;
	}*/

	@Override
	public void setParentAppliImpl(Object appliImplModel) {
		if(appliImplModel instanceof DocumentModel){
			parentAppliImplModel = (DocumentModel) appliImplModel;
		}
	}
    
}
