package com.example.friendfriend


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.friendfriend.databinding.FragmentHomeBinding

/**
 * A simple [Fragment] subclass.
 */
class Home : Fragment() {

  

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater,
            R.layout.fragment_home, container, false
        )

        binding.messagecard.setOnClickListener{
            view!!.findNavController().navigate(R.id.action_home_to_chatRoom)
        }
        binding.friendcard.setOnClickListener{
            view!!.findNavController().navigate(R.id.action_home2_to_addedFriend2)
        }
        binding.schedulecard.setOnClickListener{
            view!!.findNavController().navigate(R.id.action_home_to_confession_Page2)
        }
        binding.confessionPagecard.setOnClickListener{
            view!!.findNavController().navigate(R.id.action_home_to_confession_Page2)
        }
        (activity as AppCompatActivity).supportActionBar?.title = "Home"
        setHasOptionsMenu(true)


        return binding.root

    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            view!!.findNavController()
        )
                || super.onOptionsItemSelected(item)
    }
    fun confess(view: View) {
        view.findNavController().navigate(R.id.action_home_to_confession_Page2)
    }


}
