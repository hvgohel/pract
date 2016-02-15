NamespaceManager = {
    register : function(ns)
    {
        if (ns.length > 0)
        {
            var myBaseNs = ns.substring(0, ns.lastIndexOf('.'));
            this.register(myBaseNs);
            eval("if(!window." + ns + ") window." + ns + " ={};");
        }
    }
};