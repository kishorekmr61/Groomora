package com.groomora.feature.beauty

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockBeautyRepository : BeautyRepository {
    private val categories = listOf(
        BeautyCategory("facial", "Facials & Cleanup", "face", "Deep cleansing, fruit, gold & hydra facials"),
        BeautyCategory("waxing", "Waxing", "spa", "Rica, honey, and chocolate waxing for smooth skin"),
        BeautyCategory("skin", "Skin Care & Detan", "brush", "Tan removal, peel off, and brightening treatments"),
        BeautyCategory("nails", "Nail Art & Spa", "palette", "Gel nails, acrylics, classic mani & pedi"),
        BeautyCategory("hair_treatment", "Hair Spa & Botox", "auto_awesome", "Keratin, hair botox, smoothening & deep spa")
    )

    private val services = listOf(
        // Facials
        BeautyService("b_f1", "facial", "O3+ Bridal Glow Facial", "Oxygenating facial for instant radiant glow and hyperpigmentation reduction.", 1499.0, "60 min", listOf("Instant Glow", "Detan", "Deep Hydration"), isPopular = true),
        BeautyService("b_f2", "facial", "Hydra Deep Cleanse Facial", "Multi-step treatment with gentle exfoliation and antioxidant serum infusion.", 1899.0, "50 min", listOf("Pore Tightening", "Blackhead Removal")),
        BeautyService("b_f3", "facial", "Organic Fruit Refresh Cleanup", "Gentle herbal fruit enzymes to remove dirt and restore natural softness.", 699.0, "35 min", listOf("Mild Exfoliation", "Refreshing")),
        
        // Waxing
        BeautyService("b_w1", "waxing", "Full Arms + Full Legs + Underarms (Rica Wax)", "Painless Italian Rica liposoluble wax with soothing post-wax serum.", 899.0, "45 min", listOf("Painless", "Slow Regrowth"), isPopular = true),
        BeautyService("b_w2", "waxing", "Full Body Honey Wax", "Gentle full body waxing with natural honey formulation.", 1299.0, "75 min", listOf("Complete Smoothness")),
        
        // Skin Care
        BeautyService("b_s1", "skin", "O3+ Face & Neck Detan Pack", "Concentrated botanical detan formula to reverse sun damage and dullness.", 499.0, "25 min", listOf("Tan Removal", "Cooling Sensation")),
        BeautyService("b_s2", "skin", "Korean Glass Skin Facial Peel", "AHA/BHA clarifying peel with peptide sheet mask.", 2199.0, "60 min", listOf("Glass Finish", "Fine Lines Softening"), isPopular = true),
        
        // Nails
        BeautyService("b_n1", "nails", "Deluxe Rose Petal Pedicure & Manicure", "Aromatherapy foot soak, exfoliation scrub, cuticles, and nourishing massage.", 999.0, "60 min", listOf("Relaxing Massage", "Callus Softening"), isPopular = true),
        BeautyService("b_n2", "nails", "Gel Polish & Nail Art Set", "Long-lasting gel overlay with custom ombre or chrome design.", 799.0, "45 min", listOf("3-Week Durability")),

        // Hair Treatment
        BeautyService("b_h1", "hair_treatment", "L'Oreal Mythic Oil Hair Spa", "Deep conditioning treatment with precious argan and avocado oils.", 1199.0, "50 min", listOf("Frizz Control", "Glossy Shine")),
        BeautyService("b_h2", "hair_treatment", "Keratin Intense Smooth Treatment", "Formaldehyde-free protein infusion for silky smooth, manageable hair.", 3499.0, "120 min", listOf("Zero Frizz", "Long Lasting"), isPopular = true)
    )

    private val packages = listOf(
        BeautyPackage(
            id = "bp_1",
            name = "Glow & Glam Pamper Bundle",
            description = "O3+ Facial + Rica Full Arms/Legs Waxing + Rose Pedi-Mani",
            includedServices = listOf("O3+ Bridal Glow Facial", "Full Arms + Legs Rica Wax", "Deluxe Rose Pedicure"),
            price = 2899.0,
            originalPrice = 3397.0,
            savingsPercent = 15,
            duration = "165 min"
        ),
        BeautyPackage(
            id = "bp_2",
            name = "Head-to-Toe Radiance Makeover",
            description = "Hydra Facial + Korean Peel + Mythic Oil Hair Spa + Gel Nails",
            includedServices = listOf("Hydra Deep Cleanse Facial", "Korean Glass Skin Peel", "L'Oreal Mythic Hair Spa", "Gel Polish & Art"),
            price = 4999.0,
            originalPrice = 6096.0,
            savingsPercent = 18,
            duration = "215 min"
        )
    )

    override fun getBeautyCategories(): Flow<List<BeautyCategory>> = flow {
        delay(200)
        emit(categories)
    }

    override fun getBeautyServices(categoryId: String?): Flow<List<BeautyService>> = flow {
        delay(250)
        if (categoryId != null) {
            emit(services.filter { it.categoryId == categoryId })
        } else {
            emit(services)
        }
    }

    override fun getBeautyPackages(): Flow<List<BeautyPackage>> = flow {
        delay(200)
        emit(packages)
    }
}
