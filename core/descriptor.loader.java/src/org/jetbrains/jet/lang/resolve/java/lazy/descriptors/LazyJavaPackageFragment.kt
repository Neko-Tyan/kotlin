package org.jetbrains.jet.lang.resolve.java.lazy.descriptors

import org.jetbrains.jet.lang.descriptors.NamespaceDescriptor
import org.jetbrains.jet.lang.descriptors.impl.AbstractNamespaceDescriptorImpl
import org.jetbrains.jet.lang.descriptors.impl.NamespaceDescriptorParent
import org.jetbrains.jet.lang.resolve.name.FqName
import org.jetbrains.jet.utils.emptyList
import org.jetbrains.jet.lang.resolve.java.JavaClassFinder
import org.jetbrains.jet.lang.resolve.lazy.storage.StorageManager

public class LazyJavaPackageFragment(
        storageManager: StorageManager,
        containingDeclaration: NamespaceDescriptorParent,
        finder: JavaClassFinder,
        private val _fqName: FqName
) : AbstractNamespaceDescriptorImpl(
        containingDeclaration,
        emptyList(),
        _fqName.shortName()
    ), NamespaceDescriptor, LazyJavaDescriptor {

    private val _memberScope = LazyJavaPackageFragmentScope(this, storageManager, finder)

    override fun getMemberScope() = _memberScope
    override fun getFqName() = _fqName
}
